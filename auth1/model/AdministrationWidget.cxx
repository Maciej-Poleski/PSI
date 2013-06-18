/*
 * Copyright 2013  Maciej Poleski
 */

#include "AdministrationWidget.hxx"

#include <Wt/WTableView>
#include <Wt/WPushButton>
#include <Wt/WVBoxLayout>

#include "Session.h"
#include "ItemsModel.hxx"
#include "NameItemDelegate.hxx"

AdministrationWidget::AdministrationWidget(Session& session, Wt::WContainerWidget* parent):
    WContainerWidget(parent), _session(session)
{
    _itemsModel=new ItemsModel(session,this);
    _tableView=new Wt::WTableView(this);
    _tableView->setModel(_itemsModel);
    _tableView->setAlternatingRowColors(true);
    _tableView->setItemDelegateForColumn(0,new NameItemDelegate);

    _addNewItemPushButton=new Wt::WPushButton(Wt::WString::fromUTF8("Dodaj nowy przedmiot"));
    _addNewItemPushButton->clicked().connect([this] (const Wt::WMouseEvent&)
    {
        _itemsModel->insertRow(_itemsModel->rowCount(Wt::WModelIndex()));
    });

    _layout=new Wt::WVBoxLayout();
    _layout->addWidget(_tableView);
    _layout->addWidget(_addNewItemPushButton);
    setLayout(_layout,Wt::AlignCenter);
}
