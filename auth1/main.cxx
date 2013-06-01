/*
 * Copyright (C) 2010 Emweb bvba, Kessel-Lo, Belgium.
 *
 * See the LICENSE file for terms of use.
 */
#include <Wt/WApplication>
#include <Wt/WContainerWidget>
#include <Wt/WServer>

#include <Wt/Auth/AuthModel>
#include <Wt/Auth/AuthWidget>
#include <Wt/Auth/PasswordService>

#include <Wt/WTabWidget>

#include "model/Session.h"
#include "model/OfferWidget.hxx"

class AuthApplication : public Wt::WApplication
{
public:
    AuthApplication(const Wt::WEnvironment &env)
        : Wt::WApplication(env),
          session_(appRoot() + "auth.db") {
        session_.login().changed().connect(this, &AuthApplication::authEvent);

        useStyleSheet("css/style.css");

        Wt::Auth::AuthWidget *authWidget
        = new Wt::Auth::AuthWidget(Session::auth(), session_.users(),
                                   session_.login());

        authWidget->model()->addPasswordAuth(&Session::passwordAuth());
        authWidget->setRegistrationEnabled(true);

        authWidget->processEnvironment();

        tabWidget = new Wt::WTabWidget(root());

        tabWidget->addTab(authWidget, "Zaloguj");
        tabWidget->addTab(new OfferWidget(session_),"Oferta");
    }

    void authEvent() {
        if (session_.login().loggedIn()) {
            Wt::log("notice") << "User " << session_.login().user().id()
                              << " logged in.";
            tabWidget->setTabText(0, "Wyloguj");
        } else {
            Wt::log("notice") << "User logged out.";
            tabWidget->setTabText(0, "Zaloguj");
        }
    }

private:
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