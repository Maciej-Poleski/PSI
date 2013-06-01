#ifndef OFFERWIDGET_HXX
#define OFFERWIDGET_HXX

#include <Wt/WContainerWidget>

class Session;
class OfferWidget : public Wt::WContainerWidget
{
public:
    OfferWidget(Session &session, WContainerWidget *parent = 0);
private:
    Wt::WTableView *_tableView;
    Session &_session;
};

#endif // OFFERWIDGET_HXX
