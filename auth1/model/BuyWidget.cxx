/*
 * Copyright 2013  Maciej Poleski
 */

#include "BuyWidget.hxx"

#include <Wt/WPushButton>
#include <Wt/WVBoxLayout>
#include <Wt/WHBoxLayout>
#include <Wt/WTableView>
#include <Wt/WMessageBox>
#include <Wt/WLabel>

#include "Session.h"
#include "BuyModel.hxx"

BuyWidget::BuyWidget(Session& session, Wt::WContainerWidget* parent): WContainerWidget(parent), _session(session)
{
    _tableView = new Wt::WTableView();
    _tableView->setAlternatingRowColors(true);
    _model = new BuyModel(_session,this);
    _priceLabel=new Wt::WLabel(this);

    _tableView->setModel(_model);
    _tableView->setColumnResizeEnabled(false);

    _buyButton=new Wt::WPushButton("Kup");

    _buyButton->clicked().connect(this,&BuyWidget::performTransaction);

    userChanged();
}

void BuyWidget::repopulateGui()
{
    _layout=new Wt::WVBoxLayout();
    _layout->addWidget(_tableView,1);

    _hLayout=new Wt::WHBoxLayout();
    _hLayout->addWidget(_priceLabel,1);
    _hLayout->addWidget(_buyButton);

    _layout->addLayout(_hLayout);
    setLayout(_layout,Wt::AlignCenter);
}

void BuyWidget::reloadData()
{
    _model->reload();
    populatePrice();
    repopulateGui();
}

void BuyWidget::userChanged()
{
    Wt::Dbo::Transaction t(_session);
    _model->userChanged();
    if(_session.user())
    {
        _session.user()->cardChanged().connect(this,&BuyWidget::reloadData);
    }
    populatePrice();
    repopulateGui();
}

void BuyWidget::populatePrice()
{
    Wt::Dbo::Transaction t(_session);
    if(_session.user())
    {
        _priceLabel->setText(Wt::WString::fromUTF8("Cena: {1}").arg(_session.user()->totalPrice()));
    }
    else
    {
        _priceLabel->setText(Wt::WString::fromUTF8("Cena: 0"));
    }
}

void BuyWidget::performTransaction()
{
    if(Wt::WMessageBox::show(
                "Kup",
                Wt::WString::fromUTF8("Czy napewno chcesz dokonać transakcji?"),
                Wt::Yes | Wt::No
            )==Wt::Yes)
    {
        Wt::Dbo::Transaction t(_session);
        if(_session.user().modify()->performTransaction())
        {
            Wt::StandardButton result=Wt::WMessageBox::show(
                                          "Kup",
                                          Wt::WString::fromUTF8("Transakcja wykonana pomyślnie"),
                                          Wt::Ok);
            reloadData();
        }
        else
        {
            Wt::StandardButton result=Wt::WMessageBox::show(
                                          "Kup",
                                          Wt::WString::fromUTF8("Transakcja nie powiodła się"),
                                          Wt::Ok);
            reloadData();
        }
    }
}
