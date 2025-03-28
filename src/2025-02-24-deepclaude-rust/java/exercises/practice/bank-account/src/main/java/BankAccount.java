class BankAccount {
    private int balance;
    private boolean isOpen;

    synchronized void open() throws BankAccountActionInvalidException {
        if (isOpen) {
            throw new BankAccountActionInvalidException("Account is already open");
        }
        isOpen = true;
        balance = 0;
    }

    synchronized void close() throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account is not open");
        }
        if (balance != 0) {
            throw new BankAccountActionInvalidException("Cannot close account with non-zero balance");
        }
        isOpen = false;
    }

    synchronized int getBalance() throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account is closed");
        }
        return balance;
    }

    synchronized void deposit(int amount) throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account is closed");
        }
        if (amount <= 0) {
            throw new BankAccountActionInvalidException("Cannot deposit or withdraw non-positive amount");
        }
        balance += amount;
    }

    synchronized void withdraw(int amount) throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account is closed");
        }
        if (amount <= 0) {
            throw new BankAccountActionInvalidException("Cannot deposit or withdraw non-positive amount");
        }
        if (balance < amount) {
            throw new BankAccountActionInvalidException("Cannot withdraw more than balance");
        }
        balance -= amount;
    }

}
