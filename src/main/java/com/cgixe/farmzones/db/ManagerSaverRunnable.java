package com.cgixe.farmzones.db;

import org.bukkit.Bukkit;

public class ManagerSaverRunnable implements Runnable {
    @Override
    public void run() {
        ManagerSaver.SaveManager();
    }
}
