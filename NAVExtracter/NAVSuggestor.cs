using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;
using NAVSuggestor;

namespace NAVSuggestor
{
    public class NAVSuggestor
    {
        private Dictionary<string, Item> items;
        private Dictionary<string, Invoice> invoices;
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
            // Fetch data from NAV web service
            GetData();
            // Initialize a suggestor instance with TFIDF as the recommender engine.
            // Pre-Calculations are done in constructor

            Dictionary<string, SuggestorCollection> testInvoices = invoices.ToDictionary(x => (x.Key), x => (SuggestorCollection)x.Value);
            Dictionary<string, SuggestorItem> testItems = items.ToDictionary(x => (x.Key), x => (SuggestorItem)x.Value);
            Dictionary<string, SuggestorUser> testUsers = users.ToDictionary(x => (x.Key), x => (SuggestorUser)x.Value);

            suggestor = new SuggestorConnector(testInvoices, testItems, testUsers, new Suggestor.Algorithms.TFIDF(10));
            suggestor.Initialize();
        }

        public void Foo(SuggestorCollection collection)
        {

        }

        public Dictionary<string, double> GetRecommendedItemsScore(Invoice compareInvoice, int n)
        {
            return suggestor.SuggestItems(compareInvoice, n);
            //return suggestor.GetRecommendedItems(compareInvoice);
        }

        public List<Item> GetRecommendedItems(Invoice compareInvoice, int n)
        {
            Dictionary<string, double> recommendedScores = suggestor.SuggestItems(compareInvoice, n);
            List<Item> recommendedItems = new List<Item>();
            foreach (string id in recommendedScores.Keys)
            {
                recommendedItems.Add(GetItems()[id]);
            }
            return recommendedItems;
        }

        public void GetData()
        {
            NAVExtracter extracter = new NAVExtracter();
            string dateFilter = "1.1.2005..1.2.2010";
            items = extracter.GetItems(dateFilter);

            Tuple<Dictionary<string, User>, Dictionary<string, Invoice>> invoicesAndCustomers = extracter.GetInvoices(dateFilter);
            invoices = invoicesAndCustomers.Item2;
            users = invoicesAndCustomers.Item1;
        }

        #region Basket/Getters

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
                currentInvoice.InvoiceLines.Add(itemIdAndItem.Key,
                         new InvoiceLine(itemIdAndItem.Key, GetItems()[itemIdAndItem.Key].Description, itemIdAndItem.Value.Quantity));
            }
            return currentInvoice;
        }

        public void AddBasketItem(string itemId, int quantity)
        {
            if (currentItems.ContainsKey(itemId))
            {
                currentItems[itemId].Quantity = currentItems[itemId].Quantity + quantity;
            }
            else
            {
                Item itemToAdd = (Item)items[itemId].DeepCopy();
                itemToAdd.Quantity = quantity;
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

        public void ClearBasket()
        {
            currentItems.Clear();
        }

        #endregion
    }
}
