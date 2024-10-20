/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2012-2020, Pylo
 * Copyright (C) 2020-2021, Pylo, opensource contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rip.sayori.rmcr.java;

import org.fife.rsta.ac.java.buildpath.LibraryInfo;
import rip.sayori.rmcr.generator.Generator;
import rip.sayori.rmcr.generator.GeneratorFlavor;
import rip.sayori.rmcr.generator.GeneratorGradleCache;
import rip.sayori.rmcr.gradle.GradleCacheImportFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fife.rsta.ac.java.JarManager;
import org.fife.rsta.ac.java.buildpath.DirSourceLocation;
import org.fife.rsta.ac.java.buildpath.JarLibraryInfo;
import org.fife.rsta.ac.java.buildpath.ZipSourceLocation;
import org.gradle.tooling.BuildException;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.ExternalDependency;
import org.gradle.tooling.model.eclipse.EclipseProject;
import rip.sayori.rmcr.gradle.GradleUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ProjectJarManager extends JarManager {

	private static final Logger LOG = LogManager.getLogger("Jar Manager");

	private final List<GeneratorGradleCache.ClasspathEntry> classpath;

	public ProjectJarManager(Generator generator) {
		if (generator.getGeneratorConfiguration().getGeneratorFlavor().getBaseLanguage()
				== GeneratorFlavor.BaseLanguage.JAVA)
			try {
				addClassFileSource(getJVMLibraryInfo());
			} catch (IOException e) {
				LOG.warn("Failed to load JVM to JAR manager", e);
				throw new RuntimeException(e);
			}

		this.classpath = loadClassPathJARs(generator);
	}

	private static LibraryInfo getJVMLibraryInfo() {
		File jreHome = new File(System.getProperty("java.home"));

		final File classesArchive = findExistingPath(new File(GradleUtils.getJavaHome()), "lib/rt.jar", "../Classes/classes.jar",
				"jmods/java.base.jmod", "lib/modules");
		if (classesArchive == null) {
			LOG.warn("Failed to load default JRE JAR info");
			return null;
		}

		final LibraryInfo info;

		if (classesArchive.getName().endsWith(".jmod") || classesArchive.getName().equals("modules")) {
			try {
				info = new JarLibraryInfo(prepareModule(classesArchive));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			info = new JarLibraryInfo(classesArchive);
		}

		final File sourcesArchive = findExistingPath(jreHome, "lib/src.zip", "lib/src.jar", "src.zip", "../src.zip",
				"src.jar", "../src.jar");
		if (sourcesArchive != null) {
			info.setSourceLocation(new ZipSourceLocation(sourcesArchive));
		}

		return info;
	}
	private static File findExistingPath(final File baseDir, String... paths) {
		for (final String path : paths) {
			File file = new File(baseDir, path);
			if (file.exists())
				return file;
		}
		return null;
	}
	public static synchronized File prepareModule(File modulePath) throws IOException {
		File tgt = new File(Files.createTempFile("jmod-rmcr_",".jar").toUri());
		tgt.deleteOnExit();
		try{
			try (ZipFile moduleFile = new ZipFile(modulePath)) {
				ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(tgt));
				for(Enumeration<? extends ZipEntry> enums = moduleFile.entries(); enums.hasMoreElements();){
					ZipEntry entry = enums.nextElement();
					String name = entry.getName();
					if((name.startsWith("classes") && !name.contains("module-info")) || name.startsWith("resources")){
						zipOutputStream.putNextEntry(new ZipEntry(name.substring(name.indexOf('/') + 1)));
						InputStream in = moduleFile.getInputStream(entry);
						while(in.available() > 0)
							zipOutputStream.write(in.read());
						zipOutputStream.flush();
					}
				}
				zipOutputStream.close();
			}
		}
		catch(Exception e){
			LOG.error(e.getLocalizedMessage());
		}
		return tgt;
	}
	public ProjectJarManager(Generator generator, List<GeneratorGradleCache.ClasspathEntry> classPathEntries)
			throws GradleCacheImportFailedException {
		if (generator.getGeneratorConfiguration().getGeneratorFlavor().getBaseLanguage()
				== GeneratorFlavor.BaseLanguage.JAVA)
			try {
				addClassFileSource(getJVMLibraryInfo());
			} catch (IOException e) {
				LOG.warn("Failed to load JVM to JAR manager", e);
				throw new RuntimeException(e);
			}

		for (GeneratorGradleCache.ClasspathEntry classpathEntry : classPathEntries) {
			loadExternalDependency(classpathEntry);
		}

		this.classpath = classPathEntries;
	}

	public List<GeneratorGradleCache.ClasspathEntry> getClasspath() {
		return classpath;
	}

	private List<GeneratorGradleCache.ClasspathEntry> loadClassPathJARs(Generator generator) {
		ProjectConnection projectConnection = generator.getGradleProjectConnection();
		if (projectConnection != null) {
			List<GeneratorGradleCache.ClasspathEntry> classPathEntries = new ArrayList<>();

			try {
				EclipseProject project = projectConnection.getModel(EclipseProject.class);

				for (ExternalDependency externalDependency : project.getClasspath()) {
					if (externalDependency.getFile() != null && externalDependency.getFile().isFile()) {
						if (externalDependency.getFile().getName().startsWith("scala-"))
							continue; // skip scala libraries as we do not need them in MCreator

						if (externalDependency.getFile().getName().contains("-natives-"))
							continue; // skip native libraries as we do not need them in MCreator

						GeneratorGradleCache.ClasspathEntry classpathEntry = new GeneratorGradleCache.ClasspathEntry(
								externalDependency.getFile().getAbsolutePath(), externalDependency.getSource() != null ?
								externalDependency.getSource().getAbsolutePath() :
								null);

						classPathEntries.add(classpathEntry);

						try {
							loadExternalDependency(classpathEntry);
						} catch (GradleCacheImportFailedException ignored) {
						}
					}
				}
			} catch (BuildException ignored) {
			}

			return classPathEntries;
		}
		return Collections.emptyList();
	}

	private void loadExternalDependency(GeneratorGradleCache.ClasspathEntry classpathEntry)
			throws GradleCacheImportFailedException {
		if (!new File(classpathEntry.getLib()).exists()) {
			LOG.warn("Failed to load cached library {}", classpathEntry.getLib());
			throw new GradleCacheImportFailedException(
					new IOException("Failed to load cached library " + classpathEntry.getLib()));
		}

		JarLibraryInfo libraryInfo = new JarLibraryInfo(classpathEntry.getLib());
		if (classpathEntry.getSrc() != null) {
			if (new File(classpathEntry.getSrc()).isFile()) {
				libraryInfo.setSourceLocation(new ZipSourceLocation(classpathEntry.getSrc()));
			} else if (new File(classpathEntry.getSrc()).isDirectory()) {
				libraryInfo.setSourceLocation(new DirSourceLocation(classpathEntry.getSrc()));
			}
		}
		try {
			addClassFileSource(libraryInfo);
		} catch (IOException e) {
			LOG.warn("Failed to load classpath file {}", classpathEntry.getLib(), e);
			throw new GradleCacheImportFailedException(
					new IOException("Failed to load classpath file " + classpathEntry.getLib()));
		}
	}

}
