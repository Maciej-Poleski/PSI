#include "OfferWidget.hxx"
#include "Item.hxx"
#include "Session.h"

#include <Wt/WApplication>
#include <Wt/WTableView>
#include <Wt/Dbo/QueryModel>

OfferWidget::OfferWidget(Session &session, Wt::WContainerWidget *parent): WContainerWidget(parent), _session(session)
{
    _tableView = new Wt::WTableView();
    _tableView->setAlternatingRowColors(true);
  //  Wt::Dbo::QueryModel<Wt::Dbo::ptr< Item >> *model = new Wt::Dbo::QueryModel<Wt::Dbo::ptr< Item>>(this);
   // model->setQuery(_session.find<Item>());
   // model->addAllFieldsAsColumns();
   // _tableView->setModel(model);


    addWidget(_tableView);
}
