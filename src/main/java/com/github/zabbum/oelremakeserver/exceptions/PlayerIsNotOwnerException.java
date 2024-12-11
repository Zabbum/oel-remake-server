package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelrlib.Player;
import com.github.zabbum.oelrlib.plants.industries.AbstractIndustry;

public class PlayerIsNotOwnerException extends RuntimeException {
  public PlayerIsNotOwnerException(Player player, AbstractIndustry industry) {
    super("Player " + player + " is not owner of " + industry);
  }
}
