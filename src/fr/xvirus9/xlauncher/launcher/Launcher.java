package fr.xvirus9.xlauncher.launcher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.json.CurseFileInfos;
import fr.flowarg.flowupdater.download.json.OptifineInfo;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Launcher {

	public static final GameVersion X_VERSION = new GameVersion("1.8.9", GameType.V1_8_HIGHER);
	public static final GameInfos X_INFOS = new GameInfos("X-Launcher V1", X_VERSION,  new GameTweak[]{GameTweak.FORGE});
	public static final File X_DIR = X_INFOS.getGameDir();

	public static AuthInfos authInfos;

	public static void auth(String username, String password) throws AuthenticationException {
		Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
		AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
		authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
	}

	public static void updateMinecraftForge() throws Exception {

		final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
				.withName("1.8.9")
				.withSnapshot(false)
				.withVersionType(VersionType.FORGE)
				.build();
		List<CurseFileInfos> modInfo = new ArrayList<>();
		modInfo.add(new CurseFileInfos(357540, 3101595));
		final ILogger logger = new Logger("[X-Launcher]", new File(X_DIR, "log.log"));
		final UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
				.withSilentRead(true)
				.withReExtractNatives(false)
				.withEnableModsFromCurseForge(true)
				.withInstallOptifineAsMod(true)
				.build();
		final AbstractForgeVersion forge = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.OLD)
				.withForgeVersion("1.8.9-11.15.1.2318-1.8.9")
				.withVanillaVersion(version)
				.withLogger(logger)
				.withProgressCallback(CustomCallback.callback)
				.withOptifine(new OptifineInfo("1.8.9_HD_U_L5", false))
				.withCurseMods(modInfo)
				.withNoGui(true)
				.build();
		final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
				.withVersion(version)
				.withLogger(logger)
				.withUpdaterOptions(options)
				.withProgressCallback(CustomCallback.callback)
				.withForgeVersion(forge)
				.build();
		updater.update(X_DIR);
	}

	public static void launch() throws LaunchException {
		ExternalLaunchProfile profile =  MinecraftLauncher.createExternalProfile(X_INFOS, GameFolder.FLOW_UPDATER, authInfos);
		profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getLauncherPanel().getRamSelector().getRamArguments()));

		ExternalLauncher launcher = new ExternalLauncher(profile);

		Process p = launcher.launch();

		try {
			Thread.sleep(5000L);
			LauncherFrame.getInstance().setVisible(false);
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}