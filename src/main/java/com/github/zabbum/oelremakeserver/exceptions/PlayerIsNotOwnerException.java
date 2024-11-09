package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.plants.industries.AbstractIndustry;

public class PlayerIsNotOwnerException extends RuntimeException {
  public PlayerIsNotOwnerException(Player player, AbstractIndustry industry) {
    super("Player " + player + " is not owner of " + industry);
  }
}
