package net.giuse.economymodule.events;

import net.giuse.economymodule.EconomyModule;
import net.giuse.mainmodule.MainModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class CreateAccountOnJoin implements Listener {
    private final EconomyModule economyModule;
    private final MainModule mainModule;

    @Inject
    public CreateAccountOnJoin(EconomyModule economyModule,MainModule mainModule) {
        this.economyModule = economyModule;
        this.mainModule = mainModule;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (!this.economyModule.getCustomEcoManager().hasAccount(e.getPlayer().getName())) {
            this.economyModule.getCustomEcoManager().createPlayerAccount(e.getPlayer().getName());
            this.economyModule.getCustomEcoManager().depositPlayer(e.getPlayer().getName(), this.mainModule.getConfig().getDouble("money-first-time-join"));
        }
    }
}
