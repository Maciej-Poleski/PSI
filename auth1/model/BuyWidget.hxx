/*
 * Copyright 2013  Maciej Poleski
 */

#ifndef BUYWIDGET_H
#define BUYWIDGET_H

#include <Wt/WContainerWidget>

class BuyModel;
namespace Wt
{
    class WPushButton;
};
class Session;

class BuyWidget : public Wt::WContainerWidget
{
public:
    BuyWidget(Session &session, WContainerWidget* parent = 0);

    void reloadData();
    void userChanged();

private:
    void performTransaction();
    void populatePrice();

private:
    Wt::WTableView *_tableView;
    Wt::WPushButton *_buyButton;
    Session &_session;
    Wt::WVBoxLayout *_layout;
    Wt::WHBoxLayout *_hLayout;
    Wt::WLabel *_priceLabel;

    BuyModel *_model;
};

#endif // BUYWIDGET_H
