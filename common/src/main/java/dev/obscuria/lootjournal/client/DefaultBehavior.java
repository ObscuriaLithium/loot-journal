package dev.obscuria.lootjournal.client;

public enum DefaultBehavior
{
    ALL_WHITELISTED,
    ALL_BLACKLISTED;

    public boolean isBlacklisted()
    {
        return this == ALL_BLACKLISTED;
    }
}
