#include <Wt/WApplication>
#include <Wt/WTableView>
#include <Wt/WModelIndex>
#include <Wt/WEvent>
#include <Wt/WVBoxLayout>

#include <Wt/Dbo/QueryModel>

#include "OfferWidget.hxx"
#include "Item.hxx"
#include "Session.h"
#include "CardItem.hxx"

OfferWidget::OfferWidget(Session &session, Wt::WContainerWidget *parent): WContainerWidget(parent), _session(session)
{
    _tableView = new Wt::WTableView();
    _tableView->setAlternatingRowColors(true);
    _model = new Wt::Dbo::QueryModel<Wt::Dbo::ptr< Item>>(this);
    _model->setQuery(_session.find<Item>());
    _model->addColumn("name",Wt::WString::fromUTF8("Nazwa"));
    _model->addColumn("ammount",Wt::WString::fromUTF8("Dostępność"));
    _model->addColumn("price",Wt::WString::fromUTF8("Cena"));

    Item::databaseChanged.connect(this,&OfferWidget::reloadData);

    _tableView->setModel(_model);
    _tableView->setColumnResizeEnabled(false);
    _tableView->mouseWentUp().connect(this,&OfferWidget::dispatchClick);

    _layout=new Wt::WVBoxLayout();
    _layout->addWidget(_tableView);

    setLayout(_layout,Wt::AlignCenter);
}

void OfferWidget::dispatchClick(const Wt::WModelIndex & modelIndex)
{
    if(_session.user())
    {
        Wt::Dbo::Transaction t(_session);
        _session.user().modify()->addItem(_model->resultRow(modelIndex.row()));
    }
}

void OfferWidget::reloadData()
{
    _model->reload();
}