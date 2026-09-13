package com.jonatas.finance.wallet;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.Result;
import com.jonatas.finance.wallet.Wallet.Description;
import com.jonatas.finance.wallet.WalletController.CreateWalletRequest;
import com.jonatas.finance.wallet.WalletController.EditWalletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Result<Wallet> create(CreateWalletRequest request, User user) {
        Description walletName = new Description(request.name());
        if (this.alreadyExistsUserWalletWithName(user, walletName)) {
            return Result.failure(WalletErrorCode.WALLET_WITH_THIS_NAME_ALREADY_EXISTS);
        }

        if (request.mainWallet() && this.alreadyExistsMainWalletForThisUser(user)) {
            return Result.failure(WalletErrorCode.MAIN_WALLET_ALREADY_EXISTS);
        }

        Wallet wallet = new Wallet(walletName, user, request.mainWallet());
        Wallet walletCreated = this.walletRepository.save(wallet);
        return Result.success(walletCreated);
    }

    private boolean alreadyExistsUserWalletWithName(User user, Description walletName) {
        return this.walletRepository.existsByDescriptionAndUser(walletName, user);
    }

    private boolean alreadyExistsMainWalletForThisUser(User user) {
        return this.walletRepository.existsMainWalletForUser(user);
    }

    public Result<Wallet> update(Long id, EditWalletRequest request, User user) {
        Optional<Wallet> walletFound = this.walletRepository.findByIdAndUser(id, user);
        if (walletFound.isEmpty()) {
            return Result.failure(WalletErrorCode.WALLET_NOT_FOUND);
        }

        if (request.mainWallet() && this.walletRepository.existsMainWalletForUser(user, id)) {
            return Result.failure(WalletErrorCode.MAIN_WALLET_ALREADY_EXISTS);
        }

        Description walletName = new Description(request.name());
        if (this.walletRepository.existsByDescriptionAndUserNotAndId(walletName, user, id)) {
            return Result.failure(WalletErrorCode.WALLET_WITH_THIS_NAME_ALREADY_EXISTS);
        }

        Wallet wallet = new Wallet(walletName, user, request.mainWallet());
        Wallet walletEdited = walletFound.get()
                                         .change(wallet);
        this.walletRepository.save(walletEdited);
        return Result.success(walletEdited);
    }

    public List<Wallet> findAll(User user) {
        return this.walletRepository.findAllByUser(user);
    }
}
