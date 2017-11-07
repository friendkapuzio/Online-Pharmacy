package by.bsu.machulski.command;

import by.bsu.machulski.command.admin.*;
import by.bsu.machulski.command.common.*;
import by.bsu.machulski.command.doctor.*;
import by.bsu.machulski.command.pharmacist.*;
import by.bsu.machulski.type.UserRole;

import java.util.EnumSet;

public enum CommandType {
    ACCOUNT {
        {
            this.command = new AccountCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    ADD_PRODUCT {
        {
            this.command = new AddProductCommand();
            roles = EnumSet.of(UserRole.PHARMACIST);
        }
    },
    ADD_TO_CART {
        {
            this.command = new AddToCartCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    BLOCK_USER {
        {
            this.command = new BlockUserCommand();
            roles = EnumSet.of(UserRole.ADMINISTRATOR);
        }
    },
    CART {
        {
            this.command = new CartCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    CHANGE_EMAIL {
        {
            this.command = new ChangeEmailCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    CHANGE_EXPIRATION_DATE {
        {
            this.command = new ChangeExpirationDateCommand();
            roles = EnumSet.of(UserRole.DOCTOR);
        }
    },
    CHANGE_LANGUAGE {
        {
            this.command = new ChangeLanguageCommand();
            roles = EnumSet.allOf(UserRole.class);
        }
    },
    CHANGE_NAME {
        {
            this.command = new ChangeNameCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    CHANGE_ORDER_STATUS {
        {
            this.command = new ChangeOrderStatusCommand();
            roles = EnumSet.of(UserRole.ADMINISTRATOR);
        }
    },
    CHANGE_PASSWORD {
        {
            this.command = new ChangePasswordCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    CHANGE_USER_ROLE {
        {
            this.command = new ChangeUserRoleCommand();
            roles = EnumSet.of(UserRole.ADMINISTRATOR);
        }
    },
    CHECKOUT {
        {
            this.command = new CheckoutCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    CREATE_PRESCRIPTION {
        {
            this.command = new CreatePrescriptionCommand();
            roles = EnumSet.of(UserRole.DOCTOR);
        }
    },
    CREATE_PRESCRIPTION_FORM {
        {
            this.command = new CreatePrescriptionFormCommand();
            roles = EnumSet.of(UserRole.DOCTOR);
        }
    },
    DELETE_PRODUCTS {
        {
            this.command = new DeleteProductsCommand();
            roles = EnumSet.of(UserRole.PHARMACIST);
        }
    },
    EDIT_PRODUCT {
        {
            this.command = new EditProductCommand();
            roles = EnumSet.of(UserRole.PHARMACIST);
        }
    },
    EDIT_DELETED_PRODUCT {
        {
            this.command = new EditDeletedProductCommand();
            roles = EnumSet.of(UserRole.PHARMACIST);
        }
    },
    FIND_ORDERS {
        {
            this.command = new FindOrdersCommand();
            roles = EnumSet.of(UserRole.ADMINISTRATOR);
        }
    },
    FIND_PATIENT {
        {
            this.command = new FindPatientCommand();
            roles = EnumSet.of(UserRole.DOCTOR);
        }
    },
    INDEX {
        {
            this.command = new IndexCommand();
            roles = EnumSet.allOf(UserRole.class);
        }
    },
    SHOW_GIVEN_PRESCRIPTIONS {
        {
            this.command = new ShowGivenPrescriptionsCommand();
            roles = EnumSet.of(UserRole.DOCTOR);
        }
    },
    SHOW_ORDERS {
        {
            this.command = new ShowOrdersCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    SHOW_TRANSACTIONS {
        {
            this.command = new ShowTransactionsCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    PRODUCTS {
        {
            this.command = new SearchProductCommand();
            roles = EnumSet.allOf(UserRole.class);
        }
    },
    RECHARGE_BALANCE {
        {
            this.command = new RechargeBalanceCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    REGISTER {
        {
            this.command = new RegisterCommand();
            roles = EnumSet.of(UserRole.GUEST);
        }
    },
    REMOVE_FROM_CART {
        {
            this.command = new RemoveFromCartCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    RESTORE_PRODUCTS {
        {
            this.command = new RestoreProductsCommand();
            roles = EnumSet.of(UserRole.PHARMACIST);
        }
    },
    SEARCH_BLOCKED_USER {
        {
            this.command = new SearchBlockedUserCommand();
            roles = EnumSet.of(UserRole.ADMINISTRATOR);
        }
    },
    SEARCH_DELETED_PRODUCT {
        {
            this.command = new SearchDeletedProductCommand();
            roles = EnumSet.of(UserRole.PHARMACIST);
        }
    },
    SEARCH_PRODUCT {
        {
            this.command = new SearchProductCommand();
            roles = EnumSet.allOf(UserRole.class);
        }
    },
    SEARCH_USER {
        {
            this.command = new SearchUserCommand();
            roles = EnumSet.of(UserRole.ADMINISTRATOR);
        }
    },
    SEND_MONEY {
        {
            this.command = new SendMoneyCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    SHOW_PRESCRIPTIONS {
        {
            this.command = new ShowPrescriptionsCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    SIGN_IN {
        {
            this.command = new SignInCommand();
            roles = EnumSet.of(UserRole.GUEST);
        }
    },
    SIGN_OUT {
        {
            this.command = new SignOutCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    UNBLOCK_USER {
        {
            this.command = new UnblockUserCommand();
            roles = EnumSet.of(UserRole.ADMINISTRATOR);
        }
    },
    UPDATE_CART_PRODUCT {
        {
            this.command = new UpdateCartProductCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    },
    WITHDRAW_MONEY {
        {
            this.command = new WithdrawMoneyCommand();
            roles = EnumSet.range(UserRole.USER, UserRole.ADMINISTRATOR);
        }
    };

    AbstractCommand command;
    EnumSet<UserRole> roles;

    public AbstractCommand getCurrentCommand() {
        return  command;
    }

    public EnumSet<UserRole> getRoles() {
        return roles;
    }
}
