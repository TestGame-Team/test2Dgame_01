package content;

public enum PlayerAvatar {
    MALE("Male", "/test_player/male"),
    FEMALE("Female", "/test_player/female");

    private final String displayName;
    private final String resourceRoot;

    PlayerAvatar(String displayName, String resourceRoot) {
        this.displayName = displayName;
        this.resourceRoot = resourceRoot;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getResourceRoot() {
        return resourceRoot;
    }
}
