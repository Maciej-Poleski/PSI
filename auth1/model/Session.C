#include "Session.h"

#include "Wt/Auth/AuthService"
#include "Wt/Auth/HashFunction"
#include "Wt/Auth/PasswordService"
#include "Wt/Auth/PasswordStrengthValidator"
#include "Wt/Auth/PasswordVerifier"
#include "Wt/Auth/Dbo/AuthInfo"
#include "Wt/Auth/Dbo/UserDatabase"

#include "CardItem.hxx"
#include "Item.hxx"

namespace
{

Wt::Auth::AuthService myAuthService;
Wt::Auth::PasswordService myPasswordService(myAuthService);

}

void Session::configureAuth()
{
    myAuthService.setAuthTokensEnabled(true, "logincookie");

    Wt::Auth::PasswordVerifier *verifier = new Wt::Auth::PasswordVerifier();
    verifier->addHashFunction(new Wt::Auth::BCryptHashFunction(7));
    myPasswordService.setVerifier(verifier);
    myPasswordService.setAttemptThrottlingEnabled(true);
    myPasswordService.setStrengthValidator
    (new Wt::Auth::PasswordStrengthValidator());
}

Session::Session(const std::string &sqliteDb)
    : connection_(sqliteDb)
{
    connection_.setProperty("show-queries", "true");

    setConnection(connection_);

    mapClass<Item>("item");
    mapClass<CardItem>("card_item");
    mapClass<User>("user");
    mapClass<AuthInfo>("auth_info");
    mapClass<AuthInfo::AuthIdentityType>("auth_identity");
    mapClass<AuthInfo::AuthTokenType>("auth_token");

    try {
        createTables();
        add(new Item{"nazwa1",10,100});
        add(new Item{"nazwa2",100,1});
        std::cerr << "Created database." << std::endl;
    } catch (std::exception &e) {
        std::cerr << e.what() << std::endl;
        std::cerr << "Using existing database";
    }

    users_ = new UserDatabase(*this);
}

Session::~Session()
{
    delete users_;
}

Wt::Auth::AbstractUserDatabase &Session::users()
{
    return *users_;
}

dbo::ptr<User> Session::user() const
{
    if (login_.loggedIn()) {
        dbo::ptr<AuthInfo> authInfo = users_->find(login_.user());
        dbo::ptr<User> r= authInfo->user();
        if(!r)
        {
            std::cerr<<"Creating new user metadata\n";
            r=authInfo.session()->add(new User);
            dbo::collection<dbo::ptr<User>> users=authInfo.session()->find<User>();
            if(users.size()==1)
                r.modify()->_isAdministrator=true;
            authInfo.modify()->setUser(r);
        }
        return r;
    } else
        return dbo::ptr<User>();
}

const Wt::Auth::AuthService &Session::auth()
{
    return myAuthService;
}

const Wt::Auth::PasswordService &Session::passwordAuth()
{
    return myPasswordService;
}
