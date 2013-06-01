#ifndef ITEM_HXX
#define ITEM_HXX
#include <string>
#include <Wt/Dbo/Field>
#include <Wt/Dbo/Session>
#include <Wt/Dbo/SqlTraits>
#include <Wt/Dbo/Types>
#include <Wt/WGlobal>

class Item : public Wt::Dbo::Dbo<Item>
{
public:
    std::string name;
    std::size_t ammount=0;
    std::size_t price=0;
    
    template<class Action>
    void persist(Action& a)
    {
        Wt::Dbo::field(a,name,"name");
        Wt::Dbo::field(a,ammount,"ammount");
        Wt::Dbo::field(a,price,"price");
    }
};

// DBO_EXTERN_TEMPLATES(Item);
/*
namespace Wt
{
    namespace Dbo
    {
        template<>
        class query_result_traits<ptr<Item>>
        {
        public:
            static void add(Session &session,ptr<Item> &result)
            {
//                 session.add(result);
            }
            
            static ptr<Item> create()
            {
//                 return ptr<Item>(new Item);
            }
            
            static ptr<Item> findById(Session & session, long long id)
            {
//                 return ptr<Item>();
            }
            
            static void getFields(Session &session, std::vector<std::string> *aliases, std::vector<FieldInfo> &result)
            {
                
            }
            
            static void getValues(const ptr<Item> &result, std::vector<boost::any> &values)
            {
//                 values.push_back(result->name);
//                 values.push_back(result->ammount);
//                 values.push_back(result->price);
            };
            
            static long long id(const ptr<Item> &result)
            {
                return -1;
            }
            
            static ptr<Item> load(Session &session, SqlStatement &statement,int &column)
            {
//                 return ptr<Item>();
            }
            
            static void remove(ptr<Item> &result)
            {
            }
            
            static void setValue(ptr<Item> &result,int &index, const boost::any &value)
            {
            }
        };
    };
};*/

#endif // ITEM_HXX
