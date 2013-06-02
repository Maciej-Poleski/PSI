/*
 * Copyright 2013  Maciej Poleski
 */

#ifndef CARDWIDGET_H
#define CARDWIDGET_H

#include <Wt/WContainerWidget>

#include <boost/tuple/tuple.hpp>

class CardItem;
class Session;
class Item;

class CardWidget : public Wt::WContainerWidget
{
public:
    explicit CardWidget(Session &session, Wt::WContainerWidget* parent=nullptr);

    void reloadData();
    void userChanged();

private:
    void dispatchClick(const Wt::WModelIndex& modelIndex);

private:
    Wt::Dbo::QueryModel<boost::tuple<Wt::Dbo::ptr< Item >,Wt::Dbo::ptr<CardItem>,std::int64_t>> *_model;
    Wt::WTableView *_tableView;
    Session &_session;
    Wt::WVBoxLayout *_layout;

};

#endif // CARDWIDGET_H
