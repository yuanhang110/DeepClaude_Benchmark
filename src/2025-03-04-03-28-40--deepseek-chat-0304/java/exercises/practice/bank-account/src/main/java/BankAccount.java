public class BankAccount {
    private volatile int balance;
    private volatile boolean isOpen;
    
    public BankAccount() {
        isOpen = false;
        balance = 0;
    }

    public void open() throws BankAccountActionInvalidException {
        synchronized (this) {
            if (isOpen) {
                throw new BankAccountActionInvalidException("Account already open");
            }
            isOpen = true;
            balance = 0;
        }
    }

    void close() throws BankAccountActionInvalidException {
        synchronized (this) {
            if (!isOpen) {
                throw new BankAccountActionInvalidException("Account already closed");
            }
            isOpen = false;
        }
    }

    synchronized int getBalance() throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account closed");
        }
        return balance;
    }

    synchronized void deposit(int amount) throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account closed");
        }
        if (amount < 0) {
            throw new BankAccountActionInvalidException("Cannot deposit negative amount");
        }
        balance += amount;
    }

    synchronized void withdraw(int amount) throws BankAccountActionInvalidException {
        if (!isOpen) {
            throw new BankAccountActionInvalidException("Account closed");
        }
        if (amount < 0) {
            throw new BankAccountActionInvalidException("Cannot withdraw negative amount");
        }
        if (balance == 0) {
            throw new BankAccountActionInvalidException("Cannot withdraw money from an empty account");
        }
        if (amount > balance) {
            throw new BankAccountActionInvalidException("Cannot withdraw more money than is currently in the account");
        }
        balance -= amount;
    }
}
