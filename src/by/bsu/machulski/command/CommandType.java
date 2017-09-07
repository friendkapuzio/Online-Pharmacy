package by.bsu.machulski.command;

import by.bsu.machulski.command.pharmacist.AddProductCommand;
import by.bsu.machulski.command.pharmacist.EditProductCommand;

public enum CommandType {
    ADD_PRODUCT {
        {
            this.command = new AddProductCommand();
        }
    },
    CHANGE_LANGUAGE {
        {
            this.command = new ChangeLanguageCommand();
        }
    },
    EDIT_PRODUCT {
        {
            this.command = new EditProductCommand();
        }
    },
    INDEX {
        {
            this.command = new IndexCommand();
        }
    },
    PRODUCTS {
        {
            this.command = new SearchProductCommand();
        }
    },
    REGISTER {
        {
            this.command = new RegisterCommand();
        }
    },
    SEARCH_PRODUCT {
        {
            this.command = new SearchProductCommand();
        }
    },
    SIGN_IN {
        {
            this.command = new SignInCommand();
        }
    },
    SIGN_OUT {
        {
            this.command = new SignOutCommand();
        }
    };
    AbstractCommand command;
    public AbstractCommand getCurrentCommand() {
        return  command;
    }

    public static boolean contains(String command) {
        boolean isContains;
        try {
            valueOf(command.toUpperCase());
            isContains = true;
        } catch (IllegalArgumentException e) {
            isContains = false;
        }
        return isContains;
    }
}
