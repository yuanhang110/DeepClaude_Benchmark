#if !defined(BANK_ACCOUNT_H)
#define BANK_ACCOUNT_H

#include <mutex>

namespace Bankaccount {
class Bankaccount {
public:
    Bankaccount();

    bool open();
    bool close();
    bool is_closed() const;
    bool deposit(double amount);
    bool withdraw(double amount);
    double balance() const;

private:
    mutable std::mutex mtx;
    bool closed;
    double balance;
};

}  // namespace Bankaccount

#endif  // BANK_ACCOUNT_H
