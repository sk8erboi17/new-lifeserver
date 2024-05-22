package net.giuse.economymodule.service;

import net.giuse.economymodule.repository.EconomyRepository;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class EconomyService {
    @Inject
    private EconomyRepository economyRepository;

    public BigDecimal getBalance(UUID uuid) {
        return economyRepository.getBalance(uuid);
    }

    public void setBalance(UUID uuid, BigDecimal balance) {
        economyRepository.setBalance(uuid, balance);
    }

    public void deposit(UUID uuid, BigDecimal amount) {
        BigDecimal currentBalance = getBalance(uuid);
        setBalance(uuid, currentBalance.add(amount));
    }

    public void withdraw(UUID uuid, BigDecimal amount) {
        BigDecimal currentBalance = getBalance(uuid);
        setBalance(uuid, currentBalance.subtract(amount));
    }

    public boolean playerExists(UUID uuid) {
        return economyRepository.playerExists(uuid);
    }
    public Map<UUID, BigDecimal> getTopBalances() {
        return economyRepository.getTopBalances();
    }

}
