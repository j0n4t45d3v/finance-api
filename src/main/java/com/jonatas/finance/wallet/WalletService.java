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

        Wallet walletCreated = this.walletRepository.save(new Wallet(walletName, user, request.mainWallet()));
        return Result.success(walletCreated);
    }

    private boolean alreadyExistsUserWalletWithName(User user, Description walletName) {
        return this.walletRepository.existsByDescriptionAndUser(walletName, user);
    }

    private boolean alreadyExistsMainWalletForThisUser(User user) {
        return this.walletRepository.existsMainWalletForUser(user);
    }

    public EditWalletResult update(Long id, EditWalletRequest request, User user) {
        Optional<Wallet> walletFound = this.walletRepository.findByIdAndUser(id, user);
        if (walletFound.isEmpty()) {
            return new EditWalletResult.WalletNotFound();
        }

        if (request.mainWallet() && this.walletRepository.existsMainWalletForUser(user, id)) {
            return new EditWalletResult.AlreadyExistsMainWalletForUser();
        }

        Description walletName = new Description(request.name());
        if (this.walletRepository.existsByDescriptionAndUserNotAndId(walletName, user, id)) {
            return new EditWalletResult.AlreadyExistsWalletWithThisName();
        }

        Wallet wallet = walletFound.get();
        wallet.setMain(request.mainWallet());
        wallet.setDescription(walletName);
        this.walletRepository.save(wallet);
        return new EditWalletResult.Success();
    }

    public List<Wallet> findAll(User user) {
        return this.walletRepository.findAllByUser(user);
    }
}
