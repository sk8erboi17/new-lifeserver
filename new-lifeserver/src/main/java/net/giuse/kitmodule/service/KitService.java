package net.giuse.kitmodule.service;

import lombok.Getter;
import net.giuse.kitmodule.databases.KitRepository;
import net.giuse.kitmodule.dto.Kit;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Getter
public class KitService {

    @Inject
    private KitRepository kitRepository;


    public void addKit(Kit kit) {
        kitRepository.addKit(kit.getName(), kit, kit.getCoolDown());
    }

    public void removeKit(String kitName) {
        kitRepository.removeKit(kitName);
    }

    public Kit getKit(String name) {
        Optional<Kit> optionalKitElement = kitRepository.getKit(name);
        return optionalKitElement.orElse(null);
    }

    public List<Kit> getAllKits() {
        return kitRepository.getAllKits();
    }
}
