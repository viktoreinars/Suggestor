using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace NAVExtracter
{
    public class NAVSuggestor
    {
        private Dictionary<string, SuggestorItem> items;
        private Dictionary<string, SuggestorInvoice> invoices;
        private Dictionary<string, User> users;
        private SuggestorConnector suggestor;

        private Dictionary<string, Item> currentItems = new Dictionary<string, Item>();        

        private static NAVSuggestor singleton;

        private NAVSuggestor() { }

        public static NAVSuggestor GetInstance()
        {
            if (singleton == null)
            {
                singleton = new NAVSuggestor();
                singleton.Initialize();
            }
            return singleton;
        }

        private void Initialize()
        {
            GetData();
            suggestor = new SuggestorConnector(invoices, items);
            suggestor.Initialize();
        }

        public Dictionary<string, Item> GetItems()
        {
            // TODO: Might be slow, iteration
            return items.ToDictionary(x => (x.Key), x => (Item)x.Value);
        }

        public Dictionary<string, Invoice> GetInvoices()
        {
            // TODO: Might be slow, iteration
            return invoices.ToDictionary(x => (x.Key), x => (Invoice)x.Value);
        }  

        public Dictionary<string, Item> GetCurrentItems()
        {
            return currentItems;
        }

        public Invoice GetBasketInvoice()
        {
            Invoice currentInvoice = new Invoice("CompareInvoiceId", "CompareUserId");
            foreach (KeyValuePair<string, Item> itemIdAndItem in GetCurrentItems())
            {
                currentInvoice.GetInvoiceLines().Add(itemIdAndItem.Key,
                         new InvoiceLine(itemIdAndItem.Key, GetItems()[itemIdAndItem.Key].GetDescription(), itemIdAndItem.Value.GetQuantity()));
            }
            return currentInvoice;
        }

        public void AddBasketItem(string itemId, int quantity)
        {
            if (currentItems.ContainsKey(itemId))
            {
                currentItems[itemId].SetQuantity(currentItems[itemId].GetQuantity() + quantity);
            }
            else
            {
                Item itemToAdd = (Item)items[itemId].DeepCopy();
                itemToAdd.SetQuantity(quantity);
                currentItems.Add(itemId, itemToAdd);
            }
        }

        public void RemoveBasketItem(string itemId, int quantity)
        {
            if (currentItems.ContainsKey(itemId))
            {
                currentItems.Remove(itemId);
            }
        }

        public Dictionary<string, double> GetRecommendedItems(Invoice compareInvoice)
        {
            return suggestor.GetRecommendedItems(compareInvoice);
        }

        public void GetData()
        {
            NAVExtracter extracter = new NAVExtracter();
            string dateFilter = "1.1.2005..1.2.2010";
            items = extracter.GetItems(dateFilter);

            Tuple<Dictionary<string, User>, Dictionary<string, SuggestorInvoice>> invoicesAndCustomers = extracter.GetInvoices(dateFilter);
            invoices = invoicesAndCustomers.Item2;
            users = invoicesAndCustomers.Item1;
        }
    }
}
