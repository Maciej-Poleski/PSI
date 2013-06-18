/*
 * Copyright 2013  Maciej Poleski
 */

#ifndef ADMINISTRATIONWIDGET_H
#define ADMINISTRATIONWIDGET_H

#include <Wt/WContainerWidget>

class ItemsModel;
class Session;

class AdministrationWidget : public Wt::WContainerWidget
{
public:
    AdministrationWidget(Session &session, Wt::WContainerWidget* parent=0);

private:
    ItemsModel *_itemsModel;
    Wt::WTableView *_tableView;
    Wt::WPushButton *_addNewItemPushButton;
    Wt::WVBoxLayout *_layout;

    Session &_session;
};

#endif // ADMINISTRATIONWIDGET_H
