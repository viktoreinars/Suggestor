using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace NAVExtracter
{
    public class Item : SuggestorItem
    {
        private string id;
        private string description;
        private int quantity;        

        public Item(string id, string description, int quantity)
        {
            this.id = id;
            this.description = description;
            this.quantity = quantity;
        }

        public string GetId()
        {
            return id;
        }

        public string GetDescription()
        {
            return description;
        }

        public int GetQuantity()
        {
            return quantity;
        }

        public void SetId(string id)
        {
            this.id = id;
        }

        public void SetDescription(string description)
        {
            this.description = description;
        }

        public void SetQuantity(int quantity)
        {
            this.quantity = quantity;
        }

        public SuggestorItem DeepCopy()
        {
            return new Item(this.id, this.description, this.quantity);
        }
    }
}
