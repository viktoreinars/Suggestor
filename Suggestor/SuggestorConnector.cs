using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public class SuggestorConnector
    {
        //private Dictionary<SuggestorInvoice, double> similarInvoices;
        //private Dictionary<string, double> recommendedItems;
        private Dictionary<string, SuggestorItem> items;
        private Dictionary<string, SuggestorInvoice> invoices;
        //private Dictionary<string, SuggestorUser> users;

        private Dictionary<string, SuggestorItem> currentItems = new Dictionary<string, SuggestorItem>();

        public SuggestorConnector(Dictionary<string, SuggestorInvoice> invoices, Dictionary<string, SuggestorItem> items)
        {
            this.invoices = invoices;
            this.items = items;
            Initialize();
        }

        public void Initialize()
        {
            CalculateInvoiceWeights(invoices, items);
        }

        private void CalculateInvoiceWeights(Dictionary<string, SuggestorInvoice> invoices, Dictionary<string, SuggestorItem> items)
        {
            foreach (SuggestorInvoice invoice in invoices.Values)
            {
                SuggestorInvoiceFunctions.CalculateWeight(invoice, invoices.Count);
            }
        }              

        private Dictionary<SuggestorInvoice, double> FindClosestInvoices(SuggestorInvoice compareInvoice, int n)
        {
            // TODO: Disgusting code
            Dictionary<SuggestorInvoice, double> topInvoices = new Dictionary<SuggestorInvoice, double>();
            List<SuggestorInvoice> compareInvoices = invoices.Values.ToList();
            compareInvoices.Remove(compareInvoice);
            // Calculate all weights
            foreach (SuggestorInvoice invoice in compareInvoices)
            {
                topInvoices.Add(invoice, SuggestorInvoiceFunctions.Compare(invoice, compareInvoice));
            }

            List<KeyValuePair<SuggestorInvoice, double>> topValues = topInvoices.ToList();
            topValues.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            topValues.Reverse();
            Dictionary<SuggestorInvoice, double> returnInvoices = new Dictionary<SuggestorInvoice, double>();
            for (int i = 0; i < n; i++)
            {
                if (topValues[i].Value != 0)
                    returnInvoices.Add(topValues[i].Key, topValues[i].Value);
            }
            return returnInvoices;
        }

        public Dictionary<string, double> GetRecommendedItems(SuggestorInvoice compareInvoice)
        {
            if (compareInvoice.GetInvoiceLines().Count == 0) return null;

            Dictionary<string, double> itemAggregation = new Dictionary<string, double>();

            Dictionary<SuggestorInvoice, double> topInvoices = FindClosestInvoices(compareInvoice, 10);

            foreach (SuggestorInvoice invoice in topInvoices.Keys)
            {
                foreach (string itemNo in invoice.GetInvoiceLines().Keys)
                {
                    if (compareInvoice.GetInvoiceLines().ContainsKey(itemNo)) continue; // Ignore items that are already in basket
                    if (!(itemAggregation.ContainsKey(itemNo))) itemAggregation.Add(itemNo, 1);
                    itemAggregation[itemNo]++;
                    //itemAggregation[itemNo] += topInvoices[invoice] * 1.0;
                }
            }

            List<KeyValuePair<string, double>> itemAggregationList = itemAggregation.ToList();
            // Sort dictionary by value
            itemAggregationList.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            itemAggregationList.Reverse();
            itemAggregation = itemAggregationList.ToDictionary(pair => pair.Key, pair => pair.Value);
            return itemAggregation;
        }
    }
}
