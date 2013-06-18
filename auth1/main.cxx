#include <Wt/WApplication>
#include <Wt/WContainerWidget>
#include <Wt/WServer>

#include <Wt/Auth/AuthModel>
#include <Wt/Auth/AuthWidget>
#include <Wt/Auth/PasswordService>

#include <Wt/WTabWidget>

#include "model/Session.h"
#include "model/OfferWidget.hxx"
#include "model/CardWidget.hxx"
#include "model/BuyWidget.hxx"
#include "model/AdministrationWidget.hxx"

class AuthApplication : public Wt::WApplication
{
public:
    AuthApplication(const Wt::WEnvironment &env)
        : Wt::WApplication(env),
          session_(appRoot() + "auth.db") {

        Wt::Auth::AuthWidget *authWidget
            = new Wt::Auth::AuthWidget(Session::auth(), session_.users(),
                                       session_.login());
        tabWidget = new Wt::WTabWidget(root());

        tabWidget->addTab(authWidget, "Zaloguj");
        _offerWidget=new OfferWidget(session_);
        tabWidget->addTab(_offerWidget,"Oferta");
        _cardWidget=new CardWidget(session_);
        tabWidget->addTab(_cardWidget,"Koszyk");
        tabWidget->setTabHidden(2,true);
        _buyWidget=new BuyWidget(session_);
        tabWidget->addTab(_buyWidget,"Transakcja");
        tabWidget->setTabHidden(3,true);
        _adminWidget=new AdministrationWidget(session_);
        tabWidget->addTab(_adminWidget,"Administracja");
        tabWidget->setTabHidden(4,true);

        session_.login().changed().connect(this, &AuthApplication::authEvent);
        session_.login().changed().connect(_cardWidget,&CardWidget::userChanged);
        session_.login().changed().connect(_buyWidget,&BuyWidget::userChanged);

        useStyleSheet("css/style.css");

        authWidget->model()->addPasswordAuth(&Session::passwordAuth());
        authWidget->setRegistrationEnabled(true);

        authWidget->processEnvironment();
    }

    void authEvent() {
        if (session_.login().loggedIn()) {
            Wt::log("notice") << "User " << session_.login().user().id()
                              << " logged in.";
            tabWidget->setTabText(0, "Wyloguj");
            tabWidget->setTabHidden(2,false);
            tabWidget->setTabHidden(3,false);
            if(session_.user()->_isAdministrator)
                tabWidget->setTabHidden(4,false);
        } else {
            Wt::log("notice") << "User logged out.";
            tabWidget->setTabText(0, "Zaloguj");
            tabWidget->setTabHidden(2,true);
            tabWidget->setTabHidden(3,true);
            tabWidget->setTabHidden(4,true);
        }
    }

private:
    OfferWidget *_offerWidget;
    CardWidget *_cardWidget;
    BuyWidget *_buyWidget;
    AdministrationWidget *_adminWidget;

    Wt::WTabWidget *tabWidget;
    Session session_;
};

Wt::WApplication *createApplication(const Wt::WEnvironment &env)
{
    return new AuthApplication(env);
}

int main(int argc, char **argv)
{
    try {
        Wt::WServer server(argv[0]);

        server.setServerConfiguration(argc, argv, WTHTTP_CONFIGURATION);
        server.addEntryPoint(Wt::Application, createApplication);

        Session::configureAuth();

        if (server.start()) {
            Wt::WServer::waitForShutdown();
            server.stop();
        }
    } catch (Wt::WServer::Exception &e) {
        std::cerr << e.what() << std::endl;
    } catch (std::exception &e) {
        std::cerr << "exception: " << e.what() << std::endl;
    }
}
