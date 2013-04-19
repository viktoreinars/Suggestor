using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public static class SuggestorCollectionFunctions
    {
        public static double CosineScore(SuggestorCollection collection, SuggestorCollection otherCollection)
        {
            // TF-IDF
            Dictionary<string, SuggestorCollectionLine> mutualInvoiceLines = new Dictionary<string, SuggestorCollectionLine>();
            mutualInvoiceLines = collection.CollectionLines.Keys.Intersect(otherCollection.CollectionLines.Keys).ToDictionary(t => t, t => collection.CollectionLines[t]);
            double cosScore = 0;
            double termProductSum = 0;
            foreach (string lineNo in mutualInvoiceLines.Keys)
            {
                termProductSum += collection.CollectionLines[lineNo].Weight * otherCollection.CollectionLines[lineNo].Weight;
            }
            cosScore = termProductSum / (GetSize(collection) * GetLimitedSize(otherCollection, mutualInvoiceLines));
            return cosScore;
        }

        public static void CalculateTFIDF(SuggestorCollection collection, double numberOfCollections)
        {
            foreach (SuggestorCollectionLine line in collection.CollectionLines.Values)
            {
                line.Weight= ((1.0 + Math.Log(line.Quantity)) * Math.Log(numberOfCollections / line.Quantity));
            }
        }

        private static void Normalize(SuggestorCollection collection)
        {
            // Not used
            double size = GetSize(collection);
            foreach (SuggestorCollectionLine line in collection.CollectionLines.Values)
            {
                line.Weight = (line.Weight / size);
            }
        }

        public static double GetLimitedSize(SuggestorCollection collection, Dictionary<string, SuggestorCollectionLine> mutualCollectionLines)
        {
            double squaredSum = 0;
            foreach (string invoiceLineNo in mutualCollectionLines.Keys)
            {
                squaredSum += Math.Pow(collection.CollectionLines[invoiceLineNo].Weight, 2);
            }
            return Math.Sqrt(squaredSum);
        }

        public static double GetSize(SuggestorCollection collection)
        {
            double squaredSum = 0;
            foreach (SuggestorCollectionLine line in collection.CollectionLines.Values)
            {
                squaredSum += Math.Pow(line.Weight, 2);
            }
            return Math.Sqrt(squaredSum);
        }

        public static void PrintInvoice(SuggestorCollection collection)
        {
            Console.WriteLine("--------------------------");
            Console.WriteLine("Collection: " + collection.CollectionId);
            
            foreach (SuggestorCollectionLine line in collection.CollectionLines.Values)
            {
                Console.WriteLine("     " + line.Description + "    " + line.Quantity);
            }
            Console.WriteLine();
            Console.WriteLine();
        }
    }
}
