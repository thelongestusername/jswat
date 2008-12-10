/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is JSwat. The Initial Developer of the Original
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2001-2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: BeepMonitor.java 6 2007-05-16 07:14:24Z nfiedler $
 */

package com.bluemarsh.jswat.core.breakpoint;

import java.awt.Toolkit;

/**
 * Produces a beep sound.
 *
 * @author  Nathan Fiedler
 */
public class BeepMonitor implements Monitor {
    /** The instance of this class. */
    private static BeepMonitor theInstance;

    static {
        theInstance = new BeepMonitor();
    }

    /**
     * Default constructor for deserialization.
     */
    private BeepMonitor() {
    }

    /**
     * Returns the single instance of this class.
     *
     * @return  the singleton instance.
     */
    public static BeepMonitor getInstance() {
        return theInstance;
    }

    public void perform(BreakpointEvent event) {
        // This does not always make a sound, even though audio may be
        // configured on the system (e.g. Fedora Core Linux).
        Toolkit.getDefaultToolkit().beep();
    }

    public boolean requiresThread() {
        return false;
    }
}