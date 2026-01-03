import os
import sys
import io
import torch
import argparse
import warnings
from PIL import Image
from torchvision import transforms
from torchvision.models import resnet50, ResNet50_Weights # Thêm dòng này
from model import MultiLabelRetinaModel

# Tắt cảnh báo để không làm nhiễu kết quả Java đọc
warnings.filterwarnings("ignore")
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

CLASS_NAMES = ["Đục thủy tinh thể", "Bệnh võng mạc tiểu đường", "Glaucoma", "Thoái hóa điểm vàng", "Cận thị nặng", "Tăng huyết áp", "Bình thường"]

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--image", type=str, required=True)
    args = parser.parse_args()

    try:
        # Khởi tạo model với cấu trúc ResNet50 chuẩn
        model = MultiLabelRetinaModel(num_classes=7)
        
        # Nạp trọng số từ file .pt
        model_path = os.path.join("ai-service", "models", "retina_multilabel.pt")
        
        if os.path.exists(model_path):
            # weights_only=False vì file .pt của bạn có thể chứa cấu trúc class
            checkpoint = torch.load(model_path, map_location='cpu')
            if isinstance(checkpoint, dict) and 'state_dict' in checkpoint:
                model.load_state_dict(checkpoint['state_dict'])
            else:
                model.load_state_dict(checkpoint)
            model.eval()
        else:
            print("Lỗi AI: Thiếu file retina_multilabel.pt")
            return

        # Tiền xử lý ảnh
        img = Image.open(args.image).convert('RGB')
        preprocess = transforms.Compose([
            transforms.Resize((224, 224)),
            transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
        ])
        img_tensor = preprocess(img).unsqueeze(0)

        with torch.no_grad():
            logits = model(img_tensor)
            # Quan trọng: Dùng Softmax để tính % thực tế
            probs = torch.softmax(logits, dim=1)[0]
            
        max_prob, idx = torch.max(probs, 0)
        
        # Dòng này Java sẽ đọc
        print(f"Kết quả AI: {CLASS_NAMES[idx]} (Độ tin cậy: {max_prob*100:.1f}%)")

    except Exception as e:
        print(f"Lỗi AI: {str(e)}")

if __name__ == "__main__":
    main()