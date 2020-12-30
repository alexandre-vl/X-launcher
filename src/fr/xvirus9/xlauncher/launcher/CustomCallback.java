package fr.xvirus9.xlauncher.launcher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.theshark34.swinger.Swinger;

public class CustomCallback
{

    public static IProgressCallback callback = new IProgressCallback() {

        @Override
        public void init(ILogger logger) {}
        @Override
        public void step(Step step) {

        }
        @Override
        public void update(int downloaded, int max) {
            LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
            LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(downloaded);
            LauncherFrame.getInstance().getLauncherPanel().setInfoText("Telechargement des fichers " + downloaded + "/" + max + " (" +
                    Swinger.percentage(downloaded, max) + "%)");
        }
    };
}
