using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public static class SuggestorInvoiceFunctions
    {
        public static double Compare(SuggestorInvoice invoice, SuggestorInvoice otherInvoice)
        {
            // TODO: Different scoring functions
            // TD-IDF
            Dictionary<string, SuggestorInvoiceLine> mutualInvoiceLines = new Dictionary<string, SuggestorInvoiceLine>();
            mutualInvoiceLines = invoice.GetInvoiceLines().Keys.Intersect(otherInvoice.GetInvoiceLines().Keys).ToDictionary(t => t, t => invoice.GetInvoiceLines()[t]);
            double cosScore = 0;
            double termProductSum = 0;
            foreach (string lineNo in mutualInvoiceLines.Keys)
            {
                termProductSum += invoice.GetInvoiceLines()[lineNo].GetWeight() * otherInvoice.GetInvoiceLines()[lineNo].GetWeight();
            }
            cosScore = termProductSum / (GetSize(invoice) * GetLimitedSize(otherInvoice, mutualInvoiceLines));
            return cosScore;
        }

        public static void CalculateWeight(SuggestorInvoice invoice, double numberOfInvoices)
        {
            foreach (SuggestorInvoiceLine line in invoice.GetInvoiceLines().Values)
            {
                line.SetWeight((1.0 + Math.Log(line.GetQuantity())) * Math.Log(numberOfInvoices / line.GetQuantity()));
            }
        }

        private static void Normalize(SuggestorInvoice invoice)
        {
            // Not used
            double size = GetSize(invoice);
            foreach (SuggestorInvoiceLine line in invoice.GetInvoiceLines().Values)
            {
                line.SetWeight(line.GetWeight() / size);
            }
        }

        public static double GetLimitedSize(SuggestorInvoice invoice, Dictionary<string, SuggestorInvoiceLine> mutualInvoiceLines)
        {
            double squaredSum = 0;
            foreach (string invoiceLineNo in mutualInvoiceLines.Keys)
            {
                squaredSum += Math.Pow(invoice.GetInvoiceLines()[invoiceLineNo].GetWeight(), 2);
            }
            return Math.Sqrt(squaredSum);
        }

        public static double GetSize(SuggestorInvoice invoice)
        {
            double squaredSum = 0;
            foreach (SuggestorInvoiceLine line in invoice.GetInvoiceLines().Values)
            {
                squaredSum += Math.Pow(line.GetWeight(), 2);
            }
            return Math.Sqrt(squaredSum);
        }

        public static void PrintInvoice(SuggestorInvoice invoice)
        {
            Console.WriteLine("--------------------------");
            Console.WriteLine("Invoice: " + invoice.GetInvoiceNo());
            foreach (SuggestorInvoiceLine line in invoice.GetInvoiceLines().Values)
            {
                Console.WriteLine("     " + line.GetDescription() + "    " + line.GetQuantity());
            }
            Console.WriteLine();
            Console.WriteLine();
        }
    }
}
