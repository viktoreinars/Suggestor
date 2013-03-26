using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace NAVExtracter
{
    public class InvoiceLine : SuggestorInvoiceLine
    {
        private string itemNo;
        private string description;
        private double quantity;
        private double weight;

        public string GetItemNo()
        {
            return itemNo; 
        }

        public string GetDescription()
        {
            return description;
        }

        public double GetQuantity()
        {
            return quantity;
        }

        public double GetWeight()
        {
            return weight;
        }

        public void SetItemNo(string itemNo)
        {
            this.itemNo = itemNo;
        }

        public void SetDescription(string description)
        {
            this.description = description;
        }

        public void SetQuantity(double quantity)
        {
            this.quantity = quantity;
        }

        public void SetWeight(double weight)
        {
            this.weight = weight;
        }

        public InvoiceLine(string itemNo, string description, double quantity)
        {
            this.itemNo = itemNo;
            this.description = description;
            this.quantity = quantity;
        }
    }
}
