#ifndef OFFERWIDGET_HXX
#define OFFERWIDGET_HXX

#include <Wt/WContainerWidget>

class Session;
class Item;

class OfferWidget : public Wt::WContainerWidget
{
public:
    explicit OfferWidget(Session &session, WContainerWidget *parent = 0);

    void reloadData();

private:
    void dispatchClick(const Wt::WModelIndex& modelIndex);

private:
    Wt::Dbo::QueryModel<Wt::Dbo::ptr< Item >> *_model;
    Wt::WTableView *_tableView;
    Session &_session;
    Wt::WVBoxLayout *_layout;
};

#endif // OFFERWIDGET_HXX
