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

package rip.sayori.rmcr.ui;

import rip.sayori.rmcr.io.tree.FileNode;
import rip.sayori.rmcr.ui.ide.CodeEditorView;
import rip.sayori.rmcr.ui.ide.ProjectFileOpener;
import rip.sayori.rmcr.ui.views.NBTEditorView;
import rip.sayori.rmcr.ui.views.editor.image.ImageMakerView;
import rip.sayori.rmcr.util.DesktopUtils;

import java.io.File;

public class FileOpener {

	public static void openFile(MCreator mcreator, Object file) {
		if (CodeEditorView.isFileSupported(file.toString())) {
			if ((file instanceof FileNode)) {
				FileNode node = (FileNode) file;
				ProjectFileOpener.openCodeFileRO(mcreator, node);
			} else if ((file instanceof File)) {
				ProjectFileOpener.openCodeFile(mcreator, (File) file);
			}
		} else if (file instanceof File && ((File) file).getName().endsWith(".nbt")) {
			NBTEditorView nbtEditorView = new NBTEditorView(mcreator, (File) file);
			nbtEditorView.showView();
		} else if (file instanceof File && (((File) file).getName().endsWith(".png") || ((File) file).getName()
				.endsWith(".gif"))) {
			ImageMakerView imageMakerView = new ImageMakerView(mcreator);
			imageMakerView.openInEditMode((File) file);
			imageMakerView.showView();
		} else {
			if ((file instanceof File) && ((File) file).isFile())
				DesktopUtils.openSafe((File) file);
		}
	}

}
