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

package rip.sayori.rmcr.vcs;

import rip.sayori.rmcr.vcs.diff.MergeHandle;
import rip.sayori.rmcr.vcs.diff.ResultSide;
import rip.sayori.rmcr.workspace.Workspace;
import org.eclipse.jgit.diff.DiffEntry;

import java.io.File;

public class FileSyncHandle {

	private final String basePath;
	private byte[] remoteBytes, localBytes, baseBytes;
	private String remotePath;
	private String localPath;
	private boolean unmerged;

	FileSyncHandle(String basePath) {
		this.basePath = basePath;
	}

	public byte[] getRemoteBytes() {
		return remoteBytes;
	}

	void setRemoteBytes(byte[] remoteBytes) {
		this.remoteBytes = remoteBytes;
	}

	public byte[] getLocalBytes() {
		return localBytes;
	}

	void setLocalBytes(byte[] localBytes) {
		this.localBytes = localBytes;
	}

	public byte[] getBaseBytes() {
		return baseBytes;
	}

	void setBaseBytes(byte[] baseBytes) {
		this.baseBytes = baseBytes;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getRemotePath() {
		return remotePath;
	}

	void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getLocalPath() {
		return localPath;
	}

	void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getPath(ResultSide resultSide) {
		switch (resultSide) {
		case LOCAL:
			return localPath;
		case REMOTE:
			return remotePath;
		default:
			return basePath;
		}
	}

	public byte[] getBytes(ResultSide resultSide) {
		switch (resultSide) {
		case LOCAL:
			return localBytes;
		case REMOTE:
			return remoteBytes;
		default:
			return baseBytes;
		}
	}

	public DiffEntry.ChangeType getChangeTypeRelativeTo(ResultSide resultSide) {
		switch (resultSide) {
		case LOCAL:
			return getChangeTypeRelativeToLocal();
		case REMOTE:
			return getChangeTypeRelativeToRemote();
		default:
			return null;
		}
	}

	public DiffEntry.ChangeType getChangeTypeRelativeToLocal() {
		if (baseBytes == null)
			if (localBytes != null)
				return DiffEntry.ChangeType.ADD;

		if (baseBytes != null)
			if (localBytes == null)
				return DiffEntry.ChangeType.DELETE;

		if (baseBytes != null)
			return DiffEntry.ChangeType.MODIFY;

		return null;
	}

	public DiffEntry.ChangeType getChangeTypeRelativeToRemote() {
		if (baseBytes == null)
			if (localBytes != null)
				return DiffEntry.ChangeType.ADD;

		if (baseBytes != null)
			if (localBytes == null)
				return DiffEntry.ChangeType.DELETE;

		if (baseBytes != null)
			return DiffEntry.ChangeType.MODIFY;

		return null;
	}

	public boolean isUnmerged() {
		return unmerged;
	}

	void setUnmerged(boolean unmerged) {
		this.unmerged = unmerged;
	}

	public MergeHandle<FileSyncHandle> toPathMergeHandle() {
		return new MergeHandle<>(this, this, getChangeTypeRelativeToLocal(), getChangeTypeRelativeToRemote());
	}

	public File toFileInWorkspace(Workspace workspace, ResultSide resultSide) {
		return new File(workspace.getWorkspaceFolder(), getPath(resultSide));
	}

	@Override public boolean equals(Object o) {
		return o instanceof FileSyncHandle && ((FileSyncHandle) o).basePath.equals(basePath);
	}

	@Override public int hashCode() {
		return basePath.hashCode();
	}

	@Override public String toString() {
		return basePath + (unmerged ? " (unmerged)" : "") + " - " + (remoteBytes != null ? "R" : "") + (
				localBytes != null ? "L" : "") + (baseBytes != null ? "B" : "");
	}
}
