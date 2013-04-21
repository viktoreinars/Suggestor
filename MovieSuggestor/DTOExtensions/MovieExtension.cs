using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace MovieSuggestor
{
    public partial class Movie : SuggestorItem
    {
        string SuggestorItem.Id
        {
            get
            {
                return Id.ToString();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string Description
        {
            get
            {
                return Title;
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        [XmlIgnore]
        public int Quantity
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public SuggestorItem DeepCopy()
        {
            throw new NotImplementedException();
        }
    }
}
