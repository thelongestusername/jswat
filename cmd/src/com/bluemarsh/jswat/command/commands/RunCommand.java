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
 * The Original Software is the JSwat Command Module. The Initial Developer of the
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2009-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.jswat.command.commands;

import com.bluemarsh.jswat.command.AbstractCommand;
import com.bluemarsh.jswat.command.CommandArguments;
import com.bluemarsh.jswat.command.CommandContext;
import com.bluemarsh.jswat.command.CommandException;
import com.bluemarsh.jswat.command.MissingArgumentsException;
import com.bluemarsh.jswat.core.connect.ConnectionFactory;
import com.bluemarsh.jswat.core.connect.ConnectionProvider;
import com.bluemarsh.jswat.core.connect.JvmConnection;
import com.bluemarsh.jswat.core.path.PathManager;
import com.bluemarsh.jswat.core.path.PathProvider;
import com.bluemarsh.jswat.core.runtime.JavaRuntime;
import com.bluemarsh.jswat.core.runtime.RuntimeManager;
import com.bluemarsh.jswat.core.runtime.RuntimeProvider;
import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.util.Processes;
import com.bluemarsh.jswat.core.util.Strings;
import com.sun.jdi.connect.VMStartException;
import java.io.File;
import java.io.PrintWriter;
import org.openide.util.NbBundle;

/**
 * Launches the debuggee and resumes the VM.
 *
 * @author Nathan Fiedler
 */
public class RunCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "run";
    }

    @Override
    public void perform(CommandContext context, CommandArguments arguments)
            throws CommandException, MissingArgumentsException {

        PrintWriter writer = context.getWriter();
        Session session = context.getSession();
        if (session.isConnected()) {
            writer.println(NbBundle.getMessage(RunCommand.class,
                    "CTL_run_activeSession"));
        } else {
            // Get the previously defined class and parameters.
            String className = session.getProperty(Session.PROP_CLASS_NAME);
            String classParams = session.getProperty(Session.PROP_CLASS_PARAMS);
            if (arguments.hasMoreTokens()) {
                className = arguments.nextToken();
                // Allow reseting the parameters by not specifying any.
                classParams = "";
                if (arguments.hasMoreTokens()) {
                    arguments.returnAsIs(true);
                    classParams = arguments.rest().trim();
                }
                // Set the class and parameters in the session properties.
                session.setProperty(Session.PROP_CLASS_NAME, className);
                session.setProperty(Session.PROP_CLASS_PARAMS, classParams);
            }

            // Find the Java runtime to use for this session.
            String runtimeId = session.getProperty(Session.PROP_RUNTIME_ID);
            RuntimeManager rm = RuntimeProvider.getRuntimeManager();
            JavaRuntime runtime = null;
            if (runtimeId != null && runtimeId.length() > 0) {
                // Try to find the runtime, if it is still around.
                runtime = rm.findById(runtimeId);
            }
            if (runtime == null) {
                // Runtime not specified or it has been uninstalled.
                // Just grab whatever is available.
                runtime = rm.iterateRuntimes().next();
            }

            // Create a connection, connect and resume the session
            PathManager pm = PathProvider.getPathManager(session);
            String cp = Strings.listToString(pm.getClassPath(), File.pathSeparator);
            String javaParams = session.getProperty(Session.PROP_JAVA_PARAMS);
            if (javaParams == null) {
                javaParams = "";
            }
            // Add on the classpath. If the user already had one in the JVM
            // options, this will likely fail, but the run help indicates
            // this already, so just let it fail.
            javaParams += " -cp " + cp;
            ConnectionFactory factory = ConnectionProvider.getConnectionFactory();
            // This may throw an IllegalArgumentException.
            JvmConnection connection = factory.createLaunching(runtime,
                    javaParams, className + ' ' + classParams);
            writer.format("%s %s %s %s %s\n", runtime.getBase(), runtime.getExec(),
                    javaParams, className, classParams);
            try {
                connection.connect();
                session.connect(connection);
                session.resumeVM();
            } catch (VMStartException vmse) {
                // This can happen when the user enters an unknown JVM option
                // and the debuggee returns immediately with an error.
                // We must read the output from the JVM and display whatever
                // error messages it provided, along with the exception.
                Process proc = vmse.process();
                String output = Processes.waitFor(proc);
                String msg = NbBundle.getMessage(RunCommand.class,
                        "ERR_run_launchFailed", vmse.toString() + '\n' + output);
                writer.println(msg);
            } catch (Exception e) {
                String msg = NbBundle.getMessage(RunCommand.class,
                        "ERR_run_launchFailed", e.toString());
                writer.println(msg);
            }
        }
    }
}
