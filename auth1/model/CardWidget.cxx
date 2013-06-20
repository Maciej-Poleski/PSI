/*
 * Copyright 2013  Maciej Poleski
 */

#include "CardWidget.hxx"

#include <Wt/WApplication>
#include <Wt/WTableView>
#include <Wt/WModelIndex>
#include <Wt/WEvent>
#include <Wt/WVBoxLayout>

#include <Wt/Dbo/Session>
#include <Wt/Dbo/QueryModel>

#include <boost/tuple/tuple.hpp>

#include "Item.hxx"
#include "Session.h"
#include "CardItem.hxx"

CardWidget::CardWidget(Session& session, Wt::WContainerWidget* parent) :
    WContainerWidget(parent), _session(session)
{
    _tableView = new Wt::WTableView();
    _tableView->setAlternatingRowColors(true);

    _model = new Wt::Dbo::QueryModel<boost::tuple<Wt::Dbo::ptr< Item >,Wt::Dbo::ptr<CardItem>,std::int64_t>>(this);
    userChanged();

    _tableView->setColumnResizeEnabled(false);
    _tableView->mouseWentUp().connect(this,&CardWidget::dispatchClick);

    _layout=new Wt::WVBoxLayout();
    _layout->addWidget(_tableView);

    setLayout(_layout,Wt::AlignCenter);

}

void CardWidget::dispatchClick(const Wt::WModelIndex& modelIndex)
{
    Wt::Dbo::Transaction t(_session);
    if(_session.user())
    {
        _session.user().modify()->removeItem(boost::get<0>(_model->resultRow(modelIndex.row())));
    }
}

void CardWidget::reloadData()
{
    _model->reload();
}

void CardWidget::userChanged()
{
    Wt::Dbo::Transaction t(_session);
    if(_session.user())
    {
        _model->setQuery(
            _session.query<boost::tuple<Wt::Dbo::ptr< Item >,Wt::Dbo::ptr<CardItem>,std::int64_t>>(
                "select item, card_item, item.price*card_item.count as sum from item, card_item"
            )
            .where("card_item.user_id = ?").bind(_session.user())
            .where("item.id = card_item.item_id")
        );
        _model->addColumn("item.name",Wt::WString::fromUTF8("Nazwa"));
        _model->addColumn("item.price",Wt::WString::fromUTF8("Cena za sztuke"));
        _model->addColumn("card_item.count",Wt::WString::fromUTF8("Sztuk"));
        _model->addColumn("sum",Wt::WString::fromUTF8("Koszt"));

        _tableView->setModel(_model);
        _session.user()->cardChanged().connect(this,&CardWidget::reloadData);
    }
}
