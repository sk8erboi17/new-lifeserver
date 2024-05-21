package net.giuse.teleportmodule.submodule.subservice;

import net.giuse.teleportmodule.submodule.dto.Home;
import net.giuse.teleportmodule.submodule.subrepository.HomeRepository;

import javax.inject.Inject;
import java.util.List;

public class HomeService {

    @Inject
    private HomeRepository homeRepository;

    public void addHome(String playerUuid, String name, String location) {
        homeRepository.addHome(playerUuid, name, location);
    }

    public void updateHome(String playerUuid, String name, String newLocation) {
        homeRepository.updateHome(playerUuid, name, newLocation);
    }

    public void removeHome(String playerUuid, String name) {
        homeRepository.removeHome(playerUuid, name);
    }

    public List<Home> getAllHomes(String playerUuid) {
        return homeRepository.getAllHomes(playerUuid);
    }

    public Home getHome(String playerUuid, String name) {
        return homeRepository.getHome(playerUuid, name);
    }
}
