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

package rip.sayori.rmcr.ui.views.editor.image.tool.tools;

import rip.sayori.rmcr.ui.MCreator;
import rip.sayori.rmcr.ui.component.zoompane.ZoomedMouseEvent;
import rip.sayori.rmcr.ui.dialogs.imageeditor.ResizeCanvasDialog;
import rip.sayori.rmcr.ui.init.UIRES;
import rip.sayori.rmcr.ui.views.editor.image.canvas.Canvas;
import rip.sayori.rmcr.ui.views.editor.image.tool.component.ColorSelector;
import rip.sayori.rmcr.ui.views.editor.image.versioning.VersionManager;

import java.awt.event.MouseEvent;

public class ResizeCanvasTool extends AbstractTool {

	private final MCreator window;

	public ResizeCanvasTool(Canvas canvas, ColorSelector colorSelector, VersionManager versionManager,
			MCreator window) {
		super("Resize canvas", "A tool for resizing the main canvas", UIRES.get("img_editor.resizecanvas"), canvas,
				colorSelector, versionManager);
		this.window = window;
		noSettings(true);
	}

	@Override public boolean process(ZoomedMouseEvent e) {
		ResizeCanvasDialog dialog = new ResizeCanvasDialog(window, canvas);
		dialog.setVisible(true);
		return true;
	}

	@Override public void mouseDragged(MouseEvent e) {
	}
}
