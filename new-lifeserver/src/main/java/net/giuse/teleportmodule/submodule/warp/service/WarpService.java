package net.giuse.teleportmodule.submodule.warp.service;

import net.giuse.teleportmodule.submodule.warp.dto.Warp;
import net.giuse.teleportmodule.submodule.warp.repository.WarpRepository;

import javax.inject.Inject;
import java.util.List;

public class WarpService {

    @Inject
    private WarpRepository warpRepository;

    public void addWarp(String name, String location) {
        warpRepository.addWarp(name, location);
    }

    public void updateWarp(String name, String newLocation) {
        warpRepository.updateWarp(name, newLocation);
    }

    public void removeWarp(String name) {
        warpRepository.removeWarp(name);
    }

    public List<Warp> getAllWarps() {
        return warpRepository.getAllWarps();
    }

    public Warp getWarp(String name) {
        return warpRepository.getWarp(name);
    }
}
