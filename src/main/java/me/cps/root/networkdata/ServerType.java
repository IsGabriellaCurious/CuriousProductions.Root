package me.cps.root.networkdata;

/**
 * Curious Productions Root
 * Network Data Hub - Server Type Enum
 *
 * Enum that holds a list of all server types.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-30
 */
public enum ServerType {

    NETWORKCONFIG("https://pastebin.com/raw/0NYhDm8D", "https://github.com/IsGabriellaCurious/CuriousProductions.Root"),
    ROOT("https://pastebin.com/raw/PkFfs3qg", "https://github.com/IsGabriellaCurious/CuriousProductions.Root"),
    HUB("https://pastebin.com/raw/x5axb8SL", "https://github.com/IsGabriellaCurious/CuriousProductions.Hub"),
    GAMEMANAGER("https://pastebin.com/raw/ymKuzzzE", "https://github.com/IsGabriellaCurious/CuriousProductions.GameManager");

    private String versionUrl;
    private String githubUrl;

    private ServerType(String versionUrl, String githubUrl) {
        this.versionUrl = versionUrl;
        this.githubUrl = githubUrl;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }
}
