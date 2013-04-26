using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor.Algorithms
{
    public class CollaborativeFiltering : SuggestorRecommender
    {
        // TODO: Keep collections global here or in SuggestorConnector.

        private int defaultNoOfUsersToSuggest = 10;

        public CollaborativeFiltering(int defaultNoOfUsersToSuggest)
        {
            this.defaultNoOfUsersToSuggest = defaultNoOfUsersToSuggest;
        }

        public override void PreCalculation(Dictionary<string, SuggestorCollection> collections, Dictionary<string, SuggestorItem> items)
        {
            // No pre-calculations
        }

        public override Dictionary<string, double> SuggestNItems(Dictionary<string, SuggestorCollection> collections, SuggestorCollection compareCollection, int n)
        {
            if (compareCollection.CollectionLines.Count == 0) return null;

            Dictionary<string, double> itemAggregation = new Dictionary<string, double>();

            // Find top similar collections
            Dictionary<SuggestorCollection, double> topCollections = SuggestNCollections(collections, compareCollection, 10);

            foreach (SuggestorCollection collection in topCollections.Keys)
            {
                foreach (string itemNo in collection.CollectionLines.Keys)
                {
                    if (compareCollection.CollectionLines.ContainsKey(itemNo)) continue; // Ignore items that are already in basket
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

        public override Dictionary<SuggestorCollection, double> SuggestNCollections(Dictionary<string, SuggestorCollection> collections, SuggestorCollection compareCollection, int n)
        {
            // TODO: Disgusting code
            Dictionary<SuggestorCollection, double> topCollections = new Dictionary<SuggestorCollection, double>();
            List<SuggestorCollection> compareCollections = collections.Values.ToList();
            compareCollections.Remove(compareCollection);
            
            // TODO: Cache this. Per instance?
            foreach (SuggestorCollection invoice in compareCollections)
            {
                // Find cosine similarity between query collection and other collections
                topCollections.Add(invoice, SuggestorCollectionFunctions.CosineScore(invoice, compareCollection));
            }

            List<KeyValuePair<SuggestorCollection, double>> topValues = topCollections.ToList();
            topValues.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            topValues.Reverse();
            Dictionary<SuggestorCollection, double> returnInvoices = new Dictionary<SuggestorCollection, double>();
            for (int i = 0; i < n; i++)
            {
                if (topValues[i].Value != 0)
                    returnInvoices.Add(topValues[i].Key, topValues[i].Value);
            }
            return returnInvoices;
        }

        public override Dictionary<SuggestorUser, double> SuggestNUsers(List<SuggestorUser> users, SuggestorUser compareUser, int n)
        {
            // TODO: Disgusting code
            Dictionary<SuggestorUser, double> topUsers = new Dictionary<SuggestorUser, double>();
            List<SuggestorUser> compareUsers = users.ToList();
            compareUsers.Remove(compareUser);
            
            // TODO: Cache this. Per instance?
            foreach (SuggestorUser user in compareUsers)
            {
                // Find cosine similarity between users lines and other users lines                
                topUsers.Add(user, SuggestorCollectionFunctions.CosineScore(user, compareUser));
            }

            List<KeyValuePair<SuggestorUser, double>> topValues = topUsers.ToList();
            topValues.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            topValues.Reverse();
            Dictionary<SuggestorUser, double> returnUsers = new Dictionary<SuggestorUser, double>();
            for (int i = 0; i < n; i++)
            {
                if (topValues[i].Value != 0)
                    returnUsers.Add(topValues[i].Key, topValues[i].Value);
            }
            return returnUsers;
        }        

        /*private void CalculateInvoiceWeights(Dictionary<string, SuggestorCollection> invoices, Dictionary<string, SuggestorItem> items)
        {
            foreach (SuggestorCollection invoice in invoices.Values)
            {
                SuggestorCollectionFunctions.CalculateTFIDF(invoice, invoices.Count);
            }
        }*/
    }
}
