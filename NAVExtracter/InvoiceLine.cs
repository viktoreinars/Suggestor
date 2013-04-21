using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace NAVSuggestor
{
    public class InvoiceLine : SuggestorCollectionLine
    {
        private string itemNo;
        private string description;
        private double quantity;
        private double weight;

        public InvoiceLine() { }

        public InvoiceLine(string itemNo, string description, double quantity)
        {
            this.itemNo = itemNo;
            this.description = description;
            this.quantity = quantity;
        }

        public string Id
        {
            get
            {
                return itemNo;
            }
            set
            {
                itemNo = value;
            }
        }

        public string Description
        {
            get
            {
                return description;
            }
            set
            {
                description = value;
            }
        }

        public double Quantity
        {
            get
            {
                return quantity;
            }
            set
            {
                quantity = value;
            }
        }

        // TODO: Ignore this?
        public double Weight
        {
            get
            {
                return weight;
            }
            set
            {
                weight = value;
            }
        }
        /*
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
        }        */
    }
}
