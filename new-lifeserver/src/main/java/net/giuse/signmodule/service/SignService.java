package net.giuse.signmodule.service;

import net.giuse.signmodule.databases.SignPreviewRepository;
import net.giuse.signmodule.files.SignFileManager;
import org.bukkit.Location;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class SignService {

    @Inject
    private SignFileManager fileManager;

    @Inject
    private SignPreviewRepository signPreviewRepository;

    public List<String> getLines() {
        return fileManager.getSignYaml().getStringList("preview-lines-sign");
    }

    public void addSignKitPreview(Location location, String kitName) {
        signPreviewRepository.addSignPreview(location, kitName);
    }

    public String getKitNameByLocation(Location location) {
        Optional<String> kitName = signPreviewRepository.getKitNameByLocation(location);
        if (kitName.isPresent()) {
            return kitName.get();
        }
        throw new NullPointerException("No kit found for location " + location);
    }

    public Location getSignPreview(Location location) {
        return signPreviewRepository.getSignByLocation(location);
    }

    public void removeSignPreview(Location location) {
        signPreviewRepository.removeSignByLocation(location);
    }

}
