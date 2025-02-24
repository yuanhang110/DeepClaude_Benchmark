class BankAccount {
    private int balance = 0;
    private boolean isOpen = false;

    synchronized void open() throws BankAccountActionInvalidException {
        if (isOpen) {
            throw new BankAccountActionInvalidException("Account is already open");
        }
        isOpen = true;
    }

    synchronized void close() throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account is not open");
        }
        isOpen = false;
        balance = 0;
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
            throw new BankAccountActionInvalidException("Insufficient balance");
        }
        balance -= amount;
    }

}
