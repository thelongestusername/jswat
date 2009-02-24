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
 * The Original Software is JSwat Installer. The Initial Developer of the
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2005-2009. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.jswat.installer;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Displays the license text and forces the user to agree with it.
 *
 * @author  Nathan Fiedler
 */
public class LicensePanel extends InstallerPanel implements ItemListener {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Indicates if the user has agreed or not. */
    private boolean agreed;

    /**
     * Creates new form LicensePanel.
     */
    public LicensePanel() {
        initComponents();
        // Load the text area with the license text.
        InputStream is = ClassLoader.getSystemResourceAsStream("LICENSE.txt");
        try {
            InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
            char[] cbuf = new char[1024];
            StringWriter sw = new StringWriter();
            int bytesRead = isr.read(cbuf);
            while (bytesRead > 0) {
                sw.write(cbuf, 0, bytesRead);
                bytesRead = isr.read(cbuf);
            }
            isr.close();
            String str = sw.toString();
            licenseTextArea.append(str);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) { }
        }
        licenseTextArea.setCaretPosition(0);
        agreeRadioButton.addItemListener(this);
    }

    @Override
    public void doHide() {
    }

    @Override
    public void doShow() {
    }

    @Override
    public String getNext() {
        if (agreed) {
            return "jdk";
        } else {
            return null;
        }
    }

    @Override
    public String getPrevious() {
        return "welcome";
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        // We only listen to the agree button.
        agreed = event.getStateChange() == ItemEvent.SELECTED;
        // Update the state of the control buttons.
        Controller.getDefault().markBusy(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        agreeButtonGroup = new javax.swing.ButtonGroup();
        licenseLabel = new javax.swing.JLabel();
        licenseScrollPane = new javax.swing.JScrollPane();
        licenseTextArea = new javax.swing.JTextArea();
        agreeRadioButton = new javax.swing.JRadioButton();
        disagreeRadioButton = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(12, 12, 12, 12)));
        licenseLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/installer/Form").getString("KEY_License_Read").charAt(0));
        licenseLabel.setLabelFor(licenseTextArea);
        licenseLabel.setText(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/installer/Form").getString("LBL_License_ReadLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(licenseLabel, gridBagConstraints);

        licenseTextArea.setEditable(false);
        licenseScrollPane.setViewportView(licenseTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        add(licenseScrollPane, gridBagConstraints);

        agreeButtonGroup.add(agreeRadioButton);
        agreeRadioButton.setMnemonic(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/installer/Form").getString("KEY_License_Agree").charAt(0));
        agreeRadioButton.setText(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/installer/Form").getString("LBL_License_AgreeButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(agreeRadioButton, gridBagConstraints);

        agreeButtonGroup.add(disagreeRadioButton);
        disagreeRadioButton.setMnemonic(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/installer/Form").getString("KEY_License_Disagree").charAt(0));
        disagreeRadioButton.setText(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/installer/Form").getString("LBL_License_DisagreeButton"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(disagreeRadioButton, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup agreeButtonGroup;
    private javax.swing.JRadioButton agreeRadioButton;
    private javax.swing.JRadioButton disagreeRadioButton;
    private javax.swing.JLabel licenseLabel;
    private javax.swing.JScrollPane licenseScrollPane;
    private javax.swing.JTextArea licenseTextArea;
    // End of variables declaration//GEN-END:variables
}
