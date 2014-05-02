package org.fusesource.ide.server.karaf.core.server.subsystems;

import java.io.IOException;
import java.net.Socket;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.ide.server.karaf.core.Activator;
import org.fusesource.ide.server.karaf.core.server.ControllableKarafServerBehavior;
import org.jboss.ide.eclipse.as.core.util.LaunchCommandPreferences;
import org.jboss.ide.eclipse.as.wtp.core.server.behavior.AbstractSubsystemController;
import org.jboss.ide.eclipse.as.wtp.core.server.behavior.ControllableServerBehavior;
import org.jboss.ide.eclipse.as.wtp.core.server.behavior.IServerShutdownController;
import org.jboss.ide.eclipse.as.wtp.core.server.launch.AbstractStartJavaServerLaunchDelegate;

public class Karaf2xShutdownController extends AbstractSubsystemController
		implements IServerShutdownController {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.as.wtp.core.server.behavior.IServerShutdownController#canStop()
	 */
	@Override
	public IStatus canStop() {
		// we allow to stop servers which are started only
		if( getServer().getServerState() == IServer.STATE_STARTED) {
			return Status.OK_STATUS;
		}
		return Status.CANCEL_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.as.wtp.core.server.behavior.IServerShutdownController#stop(boolean)
	 */
	@Override
	public void stop(boolean force) {
		boolean ignoreLaunch = false;
		try {
			ILaunchConfiguration config = getServer().getLaunchConfiguration(true, new NullProgressMonitor());
			ignoreLaunch = LaunchCommandPreferences.isIgnoreLaunchCommand(config);
		} catch(CoreException ce) {
			Activator.getDefault().getLog().log(ce.getStatus());
		}
		
		if(ignoreLaunch) {
			((ControllableServerBehavior)getControllableBehavior()).setServerStopped();
			return;
		}
		stopImpl(force);
	}
	
	/**
	 * decides how to best stop the server
	 * 
	 * @param force use force?
	 */
	protected void stopImpl(boolean force) {
		int state = getServer().getServerState();
		
		// if we use force we don't look for the state and stop 
		if (force || shouldUseForce()) {
			forceStop();
		} else if (state == IServer.STATE_STARTING
				|| state == IServer.STATE_STOPPING) {
			// if we're starting up or shutting down and they've tried again,
			// then force it to stop.
			cancelPolling();
			forceStop();
		} else {
			((ControllableServerBehavior)getControllableBehavior()).setServerStopping();
			IStatus result = gracefullStop();
			if (!result.isOK()) {
				setNextStopRequiresForce(true);
				((ControllableServerBehavior)getControllableBehavior()).setServerStarted();
			}
		}
	}
	
	/**
	 * try to do a graceful stop of the server
	 * 
	 * @return
	 */
	protected IStatus gracefullStop() {
		try {
			IServerPortController ctrl = (IServerPortController)((ControllableKarafServerBehavior)getControllableBehavior()).getController("port");
			int port = ctrl.findPort(IServerPortController.KEY_MANAGEMENT_PORT, -1);
			if (port != -1) {
				// now open the port and send the shutdown command
				return shutdownKarafInstance(port);
			}			
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return Status.CANCEL_STATUS;
	}

	/**
	 * open a stream to the given port and send the shutdown command
	 * 
	 * @param managementPort
	 * @return
	 */
	protected IStatus shutdownKarafInstance(int managementPort) {
		Socket s = null;
		try {
			Karaf2xPortController ctrl = (Karaf2xPortController)((ControllableKarafServerBehavior)getControllableBehavior()).getController("port");
			// we need to obtain the shutdown command
			String shutdownCommand = ctrl.getShutdownCommand();
            s = new Socket(getServer().getHost(), managementPort);
            s.getOutputStream().write(shutdownCommand.getBytes());
		} catch (CoreException e) {
			e.printStackTrace();
			return Status.CANCEL_STATUS;
        } catch (IOException ex) {
            ex.printStackTrace();
            return Status.CANCEL_STATUS;
        } finally {
            if (s != null) {
            	try {
            		s.close();
            	} catch (IOException e) {
            		// can't close the socket
            	}
            }
        }
        return Status.OK_STATUS;
	}
	
	protected boolean shouldUseForce() {
		int state = getServer().getServerState();
		boolean useForce = !isProcessRunning() || state == IServer.STATE_STOPPED || getRequiresForce(); 
		return useForce;
	}
	
	protected void forceStop() {
		// Only synchronize on this for fast methods blocking on the process
		// Calls to parent should not be synchronized for fear of deadlock
		synchronized(this) {
			// just terminate the process.
			if( isProcessRunning()) {
				try {
					getProcess().terminate();
				} catch( DebugException e ) {
				}
			}
			clearProcess();
			setNextStopRequiresForce(false);
		}
		((ControllableServerBehavior)getControllableBehavior()).setServerStopped();
	}
	
	
	protected IProcess getProcess() {
		IProcess existing = (IProcess)getControllableBehavior().getSharedData(AbstractStartJavaServerLaunchDelegate.PROCESS);
		return existing;
	}

	protected boolean getRequiresForce() {
		Object o = getControllableBehavior().getSharedData(AbstractStartJavaServerLaunchDelegate.NEXT_STOP_REQUIRES_FORCE);
		return o == null ? false : ((Boolean)o).booleanValue();
	}
	
	protected boolean isProcessRunning() {
		boolean isProcessRunning = getProcess() != null && !getProcess().isTerminated();
		return isProcessRunning;
	}
	
	protected void clearProcess() {
		getControllableBehavior().putSharedData(AbstractStartJavaServerLaunchDelegate.PROCESS, null);
	}
	

	protected void setNextStopRequiresForce(boolean val) {
		getControllableBehavior().putSharedData(AbstractStartJavaServerLaunchDelegate.NEXT_STOP_REQUIRES_FORCE, val);
	}
	
	/**
	 * cancels the polling for the server state
	 */
	protected void cancelPolling() {
		// TODO: cancel the polling when there finally is one
	}
}
