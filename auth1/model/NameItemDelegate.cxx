/*
 * Copyright 2013  Maciej Poleski
 */

#include "NameItemDelegate.hxx"

Wt::WValidator::State NameItemDelegate::validate(const Wt::WModelIndex& index, const boost::any& editState) const
{
    try
    {
        if(boost::any_cast<Wt::WString>(editState).empty())
            return Wt::WValidator::InvalidEmpty;
        else
            return Wt::WValidator::Valid;
    }
    catch(boost::bad_any_cast)
    {
        return Wt::WValidator::Invalid;
    }
}
