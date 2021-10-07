package com.ftalk.gridchat.bankomat;

import org.springframework.stereotype.Service;

@Service
public class CashMachineService {
    private final BankService bankService;

    public CashMachineService(BankService bankService) {
        this.bankService = bankService;
    }

    public boolean authClient(Card card, Integer pinCode){
        return bankService.getPin(card.getCardNumber()).equals(pinCode);
    }
}
